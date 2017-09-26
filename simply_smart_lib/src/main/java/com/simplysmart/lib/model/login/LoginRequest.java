package com.simplysmart.lib.model.login;

/**
 * Created by chandrashekhar on 9/10/15.
 */
public class LoginRequest {

    private Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static class Session {

        private String login, password, device_id, notification_token;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getNotification_token() {
            return notification_token;
        }

        public void setNotification_token(String notification_token) {
            this.notification_token = notification_token;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
