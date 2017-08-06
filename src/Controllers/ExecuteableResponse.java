package Controllers;

public class ExecuteableResponse
{
    public boolean success;
    public String response;
    public ExecuteableResponse(boolean success)
    {
        this(success,null);
    }
    public ExecuteableResponse(boolean success,String response)
    {
        this.success = success;
        this.response = response;
    }
}