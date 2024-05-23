package com.t13max.ai.fsm;


import com.t13max.ai.Blackboard;

/**
 * @Author t13max
 * @Date 13:50 2024/5/23
 */
public class StateMachineDefault<T, S extends State<T, P>, P> implements StateMachine<T, S> {

    private boolean active;

    protected T owner;

    protected S currentState;

    protected S globalState;

    protected S lastState;

    protected P stateParam;

    protected Blackboard blackboard;

    StateMachineDefault() {
        active = false;
        blackboard = new Blackboard();
    }

    public StateMachineDefault(T owner, S initState) {
        this();
        initialState(owner, initState);
    }

    public StateMachineDefault(T owner, S initState, S globalState) {
        this();
        initialState(owner, initState, globalState);
    }

    public StateMachineDefault(T owner, S initState, S globalState, P stateParam) {
        this();
        initialState(owner, initState, globalState);
        this.stateParam = stateParam;
    }

    @Override
    public void startup() {
        active = true;
        if (currentState != null)
            currentState.enter(owner, stateParam);
    }

    public void startup(P stateParam) {
        this.stateParam = stateParam;
        startup();
    }

    @Override
    public void stop() {
        active = false;
    }

    @Override
    public void update() {
        if (!active)
            return;

        blackboard.update();

        if (globalState != null)
            globalState.update(owner, stateParam);

        if (currentState != null)
            currentState.update(owner, stateParam);
    }

    @Override
    public void changeState(S state) {
        if (state == null || state == currentState)
            return;
        if (!state.stateSwitch(currentState))
            return;
        this.lastState = this.currentState;
        if (this.currentState != null)
            this.currentState.exit(owner, stateParam);
        this.currentState = state;
        this.currentState.enter(owner, stateParam);
    }

    @Override
    public boolean revertToLastState() {
        if (this.lastState == null) {
            return false;
        }
        changeState(lastState);

        return true;
    }

    @Override
    public S getCurrentState() {
        return currentState;
    }

    @Override
    public void initialState(T owner, S initState) {
        this.owner = owner;
        this.lastState = null;
        this.currentState = initState;
    }

    @Override
    public void initialState(T owner, S initState, S globalState) {
        this.initialState(owner, initState);
        this.globalState = globalState;
    }

    @Override
    public T getOwner() {
        return owner;
    }

    @Override
    public boolean handleEvent(StateEvent event) {
        if (currentState != null && currentState.onEvent(owner, stateParam, event)) {
            return true;
        }

        return globalState != null && globalState.onEvent(owner, stateParam, event);

    }

    @Override
    public Blackboard getBlackBoard() {
        return blackboard;
    }

    public P getStateParam() {
        return this.stateParam;
    }
}
