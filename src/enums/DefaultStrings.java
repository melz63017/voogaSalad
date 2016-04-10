package enums;

public enum DefaultStrings {
	
	DEFAULT_LANGUAGE("english"),
	DEFAULT_ICON("resources/guiImages/default_icon.png"),
	ENTITY_EDITOR_NAME("EditorEntity"),
	ENVIRONMENT_EDITOR_NAME("EditorEnvironment"),
	CREATE_GAME_LABEL("createGameButtonLabel"),
	PLAY_GAME_LABEL("playGameButtonLabel"),
	LANG_LOC("propertyFiles/");



    private final String content;

    /**
     * creates default for string str
     *
     * @param str default string
     */
    DefaultStrings(String str) {
        this.content = str;
    }

    /**
     * returns default string
     *
     * @return default string for enum
     */
    public String getDefault() {
        return this.content;
    }
	
	
	
	
}