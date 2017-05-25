package State;

/**
 * Created by Kris on 02-03-2017.
 */
public interface IState {

    void update(double delta);

    State getState();

    void startState();

    void endState();

    enum State{
        None,
        Game,
        Editor
    }

}
