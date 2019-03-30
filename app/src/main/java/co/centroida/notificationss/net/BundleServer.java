package co.centroida.notificationss.net;

public class BundleServer {

    public final String[] postId;
    public final String[] postData;

    public BundleServer(String[] postData) {

        int l = postData.length;

        this.postId = new String[l];
        this.postData = postData;

        for (int i = 0; i < l; i++)
            postId[i] = String.valueOf(i);
    }

    public BundleServer(String[] postId, String[] postData) {
        this.postId = postId;
        this.postData = postData;
    }

    public BundleServer(String postId, String postData) {
        this.postId = new String[] {postId};
        this.postData = new String[] {postData};
    }

    public static BundleServer getInstance() {
        return new BundleServer(new String[]{}, new String[]{});
    }
}
