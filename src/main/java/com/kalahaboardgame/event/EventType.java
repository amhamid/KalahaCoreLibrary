package com.kalahaboardgame.event;

/**
 * This is to indicate an event type.
 *
 * Created by amhamid on 7/23/15.
 */
public enum EventType {
    INITIAL_MOVE, // technically this is the same with MOVE, however, this is important for event replay-ability. (to replay all events from beginning till end)
    MOVE,
    EMPTY,
    NOT_EMPTY

}
