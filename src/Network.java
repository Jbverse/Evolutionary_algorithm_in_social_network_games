public class Network {
    private int size;
    private String randomness;
    private int density;

    public Network(int size, String randomness, int density) {
        this.size = size;
        this.randomness = randomness;
        this.density = (density > 0 && density <= 100) ? density : 50;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getRandomness() {
        return randomness;
    }

    public void setRandomness(String randomness) {
        this.randomness = randomness;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = (density > 0 && density <= 100) ? density : 50;
    }
}
