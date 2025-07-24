package com.myflashcardsapi.flashcards_api.mappers;

public interface Mapper <X, Y>{

    X mapTo(Y y);

    Y mapFrom(X x);
}
