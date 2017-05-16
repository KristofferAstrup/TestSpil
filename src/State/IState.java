package State;

/**
 * Created by Kris on 02-03-2017.
 */
public interface IState {

    public void update(double delta);

    public State getState();

    public void startState();

    public void endState();

    public enum State{
        None,
        Game,
        Editor
    }

}
