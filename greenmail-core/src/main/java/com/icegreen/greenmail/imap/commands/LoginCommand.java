/*
 * Copyright (c) 2014 Wael Chatila / Icegreen Technologies. All Rights Reserved.
 * This software is released under the Apache license 2.0
 * This file has been modified by the copyright holder.
 * Original file can be found at http://james.apache.org
 */
package com.icegreen.greenmail.imap.commands;

import com.icegreen.greenmail.imap.ImapRequestLineReader;
import com.icegreen.greenmail.imap.ImapResponse;
import com.icegreen.greenmail.imap.ImapSession;
import com.icegreen.greenmail.imap.ProtocolException;
import com.icegreen.greenmail.user.GreenMailUser;

/**
 * Handles processing for the LOGIN imap command.
 *
 * @author Darrell DeBoer <darrell@apache.org>
 */
class LoginCommand extends NonAuthenticatedStateCommand {
    public static final String NAME = "LOGIN";
    public static final String ARGS = "<userid> <password>";

    LoginCommand() {
        super(NAME, ARGS);
    }

    /**
     * @see CommandTemplate#doProcess
     */
    @Override
    protected void doProcess(ImapRequestLineReader request,
                             ImapResponse response,
                             ImapSession session)
            throws ProtocolException {
        String userid = parser.astring(request);
        String password = parser.astring(request);
        parser.endLine(request);

        if (session.getUserManager().test(userid, password)) {
            GreenMailUser user = session.getUserManager().getUser(userid);
            session.setAuthenticated(user);
            response.commandComplete(this);

        } else {
            response.commandFailed(this, "Invalid login/password for user id "+userid);
        }
    }
}

/*
6.2.2.  LOGIN Command

   Arguments:  user name
               password

   Responses:  no specific responses for this command

   Result:     OK - login completed, now in authenticated state
               NO - login failure: user name or password rejected
               BAD - command unknown or arguments invalid

      The LOGIN command identifies the client to the server and carries
      the plaintext password authenticating this user.

   Example:    C: a001 LOGIN SMITH SESAME
               S: a001 OK LOGIN completed
*/
