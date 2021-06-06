package cn.bluemobi.server.bean;

public class RankStepDataBean {

    private int usr_id;
    private int steps;
    private String name;
    private String intro;
    private int step_plan;
    private String province;
    private String city;
    private int sex;

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getStep_plan() {
        return step_plan;
    }

    public void setStep_plan(int step_plan) {
        this.step_plan = step_plan;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "RankStepDataBean{" +
                "usr_id=" + usr_id +
                ", steps=" + steps +
                ", name='" + name + '\'' +
                ", intro='" + intro + '\'' +
                ", step_plan=" + step_plan +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", sex=" + sex +
                '}';
    }
}
