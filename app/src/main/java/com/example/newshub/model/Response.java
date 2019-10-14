package com.example.newshub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Response {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("totalResults")
    @Expose
    private long totalResults;
    @SerializedName("articles")
    @Expose
    private List<Article> articles = new ArrayList<Article>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Response() {
    }

    /**
     *
     * @param articles
     * @param totalResults
     * @param status
     */
    public Response(String status, long totalResults, List<Article> articles) {
        super();
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Response withStatus(String status) {
        this.status = status;
        return this;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public Response withTotalResults(long totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Response withArticles(List<Article> articles) {
        this.articles = articles;
        return this;
    }
}
