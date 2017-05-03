package com.michalplachta.galactic.java.service.remotedata;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="state")
@JsonSubTypes({
        @JsonSubTypes.Type(value=NotRequestedYet.class, name="NotRequestedYet"),
        @JsonSubTypes.Type(value=Loading.class, name="Loading"),
        @JsonSubTypes.Type(value=Failed.class, name="Failed"),
        @JsonSubTypes.Type(value=Fetched.class, name="Fetched")
})
public interface RemoteData<A> {}
