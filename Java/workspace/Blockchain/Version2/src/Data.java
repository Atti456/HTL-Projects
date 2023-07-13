public class Data {
    private String sender;
    private String receifer;
    private double amount;

    public Data(String sender, String receifer, double amount) {
        this.sender = sender;
        this.receifer = receifer;
        this.amount = amount;
    }

    public Data() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceifer() {
        return receifer;
    }

    public void setReceifer(String receifer) {
        this.receifer = receifer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;

        Data data = (Data) o;

        if (Double.compare(data.getAmount(), getAmount()) != 0) return false;
        if (getSender() != null ? !getSender().equals(data.getSender()) : data.getSender() != null) return false;
        return getReceifer() != null ? getReceifer().equals(data.getReceifer()) : data.getReceifer() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getSender() != null ? getSender().hashCode() : 0;
        result = 31 * result + (getReceifer() != null ? getReceifer().hashCode() : 0);
        temp = Double.doubleToLongBits(getAmount());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
