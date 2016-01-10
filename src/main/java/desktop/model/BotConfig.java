package desktop.model;

public class BotConfig {

    private int easyBotCount;
    private int hardBotCount;

    public BotConfig (int easyBotCount, int hardBotCount) {
        this.easyBotCount = easyBotCount;
        this.hardBotCount = hardBotCount;
    }

    public int getEasyBotCount() {
        return easyBotCount;
    }

    public void setEasyBotCount(int easyBotCount) {
        this.easyBotCount = easyBotCount;
    }

    public int getHardBotCount() {
        return hardBotCount;
    }

    public void setHardBotCount(int hardBotCount) {
        this.hardBotCount = hardBotCount;
    }
}
