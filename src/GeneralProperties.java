public class GeneralProperties {
    private int numOfExperiments;
    private boolean save;

    public GeneralProperties(int numOfExperiments, boolean save) {
        this.numOfExperiments = numOfExperiments;
        this.save = save;
    }

    public int getNumOfExperiments() {
        return numOfExperiments;
    }

    public void setNumOfExperiments(int numOfExperiments) {
        this.numOfExperiments = numOfExperiments;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }
}
