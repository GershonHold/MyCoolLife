package cn.bluemobi.server.bean;

public class UserInfoBean {

    private String name;
    private String intro;
    private String stepPlan;
    private int usr_id;

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

    public String getStepPlan() {
        return stepPlan;
    }

    public void setStepPlan(String stepPlan) {
        this.stepPlan = stepPlan;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }
}
