/**
 * Copyright (c) 2012 Todoroo Inc
 *
 * See the file "LICENSE" for the full license governing this code.
 */
package com.todoroo.astrid.gtasks.api;

import com.google.api.services.tasks.model.Task;

import java.io.IOException;

/**
 * Encapsulates a request to the api to move a task from one list to another
 *
 * @author Sam Bosley
 */
public class MoveListRequest extends PushRequest {

    private String idTaskToMove;
    private String dstList;
    private final String newParent;

    public MoveListRequest(GtasksInvoker service, String idTask, String srcList, String dstList, String newParent) {
        super(service, srcList, new Task());
        this.idTaskToMove = idTask;
        this.dstList = dstList;
        this.newParent = newParent;
    }

    @Override
    public Task executePush() throws IOException {
        Task localSave = service.getGtask(getListId(), idTaskToMove);
        service.deleteGtask(getListId(), idTaskToMove);
        transferProperties(localSave);
        return service.createGtask(dstList, toPush);
    }

    @Override
    protected void recover() {
        //If there's a good way to recover, put it here
        //Since MoveListRequest isn't actually used at the moment, it's probably fine for now
    }

    private void transferProperties(Task local) {
        toPush.setCompleted(local.getCompleted());
        toPush.setDeleted(local.getDeleted());
        toPush.setDue(local.getDue());
        toPush.setHidden(local.getHidden());
        toPush.setNotes(local.getNotes());
        toPush.setStatus(local.getStatus());
        toPush.setTitle(local.getTitle());

        toPush.setParent(newParent);
    }
}