package tmdbwrapper.model;

public enum MediaType {
    MOVIE("Movie"),
    TV_SHOW("Tv");
    
    private String typeIdentifier;
    
    MediaType(String typeIdentifier) {
	this.typeIdentifier = typeIdentifier;
    }
    
    @Override
    public String toString() {
	return typeIdentifier;
    }
}
