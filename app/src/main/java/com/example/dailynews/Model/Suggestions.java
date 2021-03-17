package com.example.dailynews.Model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Suggestions {

    @Expose
    private  List<Suggestion> suggestions;

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }
}
