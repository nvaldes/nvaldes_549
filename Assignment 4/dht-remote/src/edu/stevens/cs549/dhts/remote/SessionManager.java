package edu.stevens.cs549.dhts.remote;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * Maintain a stack of shells.
 * @author dduggan
 *
 */
public class SessionManager {
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SessionManager.class.getCanonicalName());
	
	public static final String ACK = "ACK";
	
	private static final SessionManager SESSION_MANAGER = new SessionManager();
	
	public static SessionManager getSessionManager() {
		return SESSION_MANAGER;
	}
	
	private Lock lock = new ReentrantLock();
	
	private ControllerServer currentServer;
	
	private CloseReason closeReason(String reason) {
		return new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, reason);
	}
	
	public boolean isSession() {
		return currentServer != null;
	}

	public Session getCurrentSession() {
		return currentServer != null ? currentServer.getSession() : null;
	}

	public boolean setCurrentSession(ControllerServer server) {
		lock.lock();
		try {
			if (currentServer == null) {
				currentServer = server;
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void acceptSession() throws IOException {
		lock.lock();
		try {
			/*
			 *  TODO We are accepting a remote control request.  Push a local shell with a proxy context
			 *  on the shell stack and flag that initialization has completed.  Confirm acceptance of the 
			 *  remote control request by sending an ACK to the client.  The CLI of the newly installed shell
			 *  will be executed by the underlying CLI as part of the "accept" command.
			 */

		} finally {
			lock.unlock();
		}
	}
	
	public void rejectSession() {
		lock.lock();
		try {
			// DONE reject remote control request by closing the session (provide a reason!)
			this.getCurrentSession().close(this.closeReason("Session Rejected"));
		} catch (IOException e) {
			logger.severe(e.getMessage());
		} finally {
			lock.unlock();
		}
	}
	
	public void closeCurrentSession() {
		lock.lock();
		try {
			// DONE normal shutdown of remote control session (provide a reason!)
			this.getCurrentSession().close(this.closeReason("Session Closed"));
		} catch (IOException e) {
			logger.severe(e.getMessage());
		} finally {
			lock.unlock();
		}
	}

}
