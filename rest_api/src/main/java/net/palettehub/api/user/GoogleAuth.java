package net.palettehub.api.user;

/**
 * Google Auth response containing the important credential field which has the JWT with user google profile.
 * 
 * @author Arthur Lewis
 */
public class GoogleAuth {
    private String clientId;
    private String credential;
    private String select_by;

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCredential() {
        return this.credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getSelect_by() {
        return this.select_by;
    }

    public void setSelect_by(String select_by) {
        this.select_by = select_by;
    }

    @Override
    public String toString() {
        return "{" +
            " clientId='" + getClientId() + "'" +
            ", credential='" + getCredential() + "'" +
            ", select_by='" + getSelect_by() + "'" +
            "}";
    }

}
