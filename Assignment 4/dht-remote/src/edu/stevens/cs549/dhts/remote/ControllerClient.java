package edu.stevens.cs549.dhts.remote;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import edu.stevens.cs549.dhts.main.IShell;
import edu.stevens.cs549.dhts.main.Log;

public class ControllerClient extends Endpoint implements MessageHandler.Whole<String> {

	public static final Logger logger = Logger.getLogger(ControllerClient.class.getCanonicalName());

	private final CountDownLatch messageLatch = new CountDownLatch(1);

	// DONE configure the client to use proper encoder for messages sent to server
	private final ClientEndpointConfig cec = ClientEndpointConfig.Builder
			.create()
			.encoders(Arrays.asList(CommandLineEncoder.class))
			.build();
	
	private final ShellManager shellManager = ShellManager.getShellManager();

	private IShell shell;

	private boolean initializing = true;
		
	private Session session;

	public ControllerClient(IShell shell) {
		this.shell = shell;
		
	}
	
	public void connect(URI uri) throws DeploymentException, IOException {
		try {
			shell.msg("Requesting control of node at " + uri.toString() + "...");
			// DONE make the connection request
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			this.session = container.connectToServer(this, cec, uri);
			while (true) {
				try {
					// Synchronize with receipt of an ack from the remote node.
					boolean connected = messageLatch.await(100, TimeUnit.SECONDS);
					if (connected && shell != shellManager.getCurrentShell()) {
						// DONE If we are connected, a new toplevel shell has been pushed, execute its CLI.
						shell = shellManager.getCurrentShell();
						shell.cli();
					}
					// Be sure to return when done, to exit the loop;
					return;

				} catch (InterruptedException e) {
					// Keep on waiting for the specified time interval
				}
			}
		} catch (IOException e) {
			shell.err(e);
		}
	}
	
	protected void endInitialization() {
		initializing = false;
		messageLatch.countDown();
	}

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		// DONE session created, add a message handler for receiving communication from server.
		// We should also cache the session for use by some of the other operations.
		this.session = session;
		this.session.addMessageHandler(this);
	}

	@Override
	public void onMessage(String message) {
		if (initializing) {
			if (SessionManager.ACK.equals(message)) {
				/*
				 * DONE server has accepted our remote control request, push a proxy shell on the shell stack
				 * and flag that initialization has finished (allowing the UI thread to continue).
				 * Make sure to replace the cached shell in this callback with the new proxy shell!
				 * 
				 * If the server rejects our request, they will just close the channel.
				 */
				IShell proxyShell = ProxyShell.createRemoteController(this.shell, this.session.getBasicRemote());
				shellManager.addShell(proxyShell);
				this.endInitialization();
				try {
					this.shell.msg("\n");
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Failure while trying to initialized session.", e);
				}

			} else {
				throw new IllegalStateException("Unexpected response to remote control request: " + message);
			}
		} else {
			// DONE provide the message to the shell
			try {
				shell.msg(message);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Failure while trying to print message.", e);
			}
		}
	}
	
	@Override
	public void onClose(Session session, CloseReason reason) {
		try {
			shell.msgln(reason.getReasonPhrase());
			shutdown();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failure while trying to report connection error.", e);
		}
	}
	
	@Override
	public void onError(Session session, Throwable t) {
		try {
			shell.err(t);
			shutdown();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failure while trying to report connection error.", t);
		}
	}
	
	protected void shutdown() throws IOException {
		/*
		 * TODO Shutdown initiated by error or closure of the connection.  Three cases: 
		 * 1. We are still initializing when this happens (need to unblock the client thread).
		 * 2. We are running an on-going remote control session (need to remove the proxy shell).
		 * 3. The remote control session has terminated (which caused the channel to be closed).
		 */
		if (initializing) {
			endInitialization();
		} else if (shell == shellManager.getCurrentShell()) {
			shellManager.removeShell();
			shell = shellManager.getCurrentShell();
			shell.cli();
		} else {
			shell = shellManager.getCurrentShell();
			shell.cli();
		}
		
	}
}
