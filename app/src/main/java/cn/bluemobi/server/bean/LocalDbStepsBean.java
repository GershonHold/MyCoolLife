package cn.bluemobi.server.bean;


import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

@Table("steps_items")
public class LocalDbStepsBean {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    @Column("date")
    private String date;
    @Column("steps")
    private int steps;
    @Column("name")
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "LocalDbStepsBean{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", steps=" + steps +
                ", name='" + name + '\'' +
                '}';
    }
}
