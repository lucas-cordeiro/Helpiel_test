package cordeiro.lucas.helpie.model;

public class Photo {
    private int id;
    private String title;
    private String url;
    private String thumbnailUrl;

    public Photo() {
        this.id = 0;
        this.title = "Null";
        this.url = "https://via.placeholder.com/600/92c952";
        this.thumbnailUrl = "https://via.placeholder.com/150/92c952";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
