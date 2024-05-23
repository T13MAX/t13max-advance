package com.t13max.ai.fsm;

import com.t13max.ai.Blackboard;

/**
 * @author zhoupeng
 * @date 2019/11/11
 * 状态机
 */
public interface StateMachine<T, S> {

    void startup();

    void stop();

    void update();

    void changeState(S state);

    boolean revertToLastState();

    S getCurrentState();

    void initialState(T owner, S initState);

    void initialState(T owner, S initState, S globalState);

    T getOwner();

    default void addState(EState<T> eState, S state) {}

    boolean handleEvent(StateEvent event);

    Blackboard getBlackBoard();
}
