import java.io.Serializable;

public class Athlet implements Comparable<Athlet>, Serializable
{
    private String vorname;
    private String nachname;
    private int gewinner;
    private String sportart;
    private float bestwert;

    public Athlet(String vorname, String nachname, int gewinner, String sportart, float bestwert) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.gewinner = gewinner;
        this.sportart = sportart;
        this.bestwert = bestwert;
    }

    public Athlet() {
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public int getGewinner() {
        return gewinner;
    }

    public void setGewinner(int gewinner) {
        this.gewinner = gewinner;
    }

    public String getSportart() {
        return sportart;
    }

    public void setSportart(String sportart) {
        this.sportart = sportart;
    }

    public float getBestwert() {
        return bestwert;
    }

    public void setBestwert(float bestwert) {
        this.bestwert = bestwert;
    }

    public String print() {
        return "Athlet{" +
                "vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", gewinner=" + gewinner +
                ", sportart='" + sportart + '\'' +
                ", bestwert='" + bestwert + '\'' +
                '}';
    }


    public String sportform() {
        return sportart;
    }


    @Override
    public int compareTo(Athlet o) {
        int ret = -1;
        if (o==null || o.getClass() != this.getClass()) ret=-1;
        else
            ret = (o.getSportart().compareTo(this.getSportart()));
        if(ret == 0)
        {
            ret = (this.getBestwert() == o.getBestwert())? 0:1;
            if(ret == 0)
            {
                ret = (this.getNachname().compareTo(o.getNachname()));
                if(ret == 0)
                {
                    ret = (this.getVorname().compareTo(o.getVorname()));
                    if(ret == 0)
                    {
                        ret = (this.getGewinner()-(o.getGewinner()));
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (!(o instanceof Athlet)) return false;

        Athlet athlet = (Athlet) o;

        if (getGewinner() != athlet.getGewinner()) return false;
        if (Float.compare(athlet.getBestwert(), getBestwert()) != 0) return false;
        if (getVorname() != null ? !getVorname().equals(athlet.getVorname()) : athlet.getVorname() != null)
            return false;
        if (getNachname() != null ? !getNachname().equals(athlet.getNachname()) : athlet.getNachname() != null)
            return false;
        return getSportart().equals(athlet.getSportart());
    }

    @Override
    public int hashCode() {
        int result = getVorname() != null ? getVorname().hashCode() : 0;
        result = 31 * result + (getNachname() != null ? getNachname().hashCode() : 0);
        result = 31 * result + getGewinner();
        result = 31 * result + getSportart().hashCode();
        result = 31 * result + (getBestwert() != +0.0f ? Float.floatToIntBits(getBestwert()) : 0);
        return result;
    }
}
