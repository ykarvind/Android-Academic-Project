package com.amurthy.todolist;

/**
 * Created by macbookpro on 3/19/17.
 */

public class Todolist {

    private String tasktitle="";
    private String description="";
    private String duedate="";
    private String information="";

    protected long id = 0;

    public long getId()
    {
        return(id);
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTasktitle()
    {
        return(tasktitle);
    }

    public void setTasktitle(String tasktitle)
    {
        this.tasktitle = tasktitle;
    }

    public String getDescription()
    {
        return(description);
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDuedate()
    {
        return(duedate);
    }

    public void setDuedate(String duedate)
    {
        this.duedate = duedate;
    }

    public String getInformation()
    {
        return(information);
    }

    public void setInformation(String information)
    {
        this.information = information;
    }

    public String toString()
    {
        return(getTasktitle());
    }
}
