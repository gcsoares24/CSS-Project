package pt.ul.fc.css.weatherwise.entities;

import java.time.LocalDateTime;

public class AuditRecord {
    private Long id;

    private int author_id;
    private int location_id;
    private LocalDateTime query_timestamp;
    private String query_type;

    public AuditRecord(int author_id, int location_id, String query_type){
        this.author_id = author_id;
        this.location_id = location_id;
        this.query_type = query_type;
    }

    public AuditRecord(){}

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }
    public int getAuthorId(){
        return author_id;
    }

    public void setAuthorId(int author_id){
        this.author_id = author_id;
    }

    public int getLocationId(){
        return location_id;
    }

    public void setLocationId(int location_id){
        this.location_id = location_id;
    }

    public LocalDateTime getQueryTimestamp(){
        return query_timestamp;
    }
    public void setQueryTimestamp(LocalDateTime query_timestamp){
        this.query_timestamp = query_timestamp;
    }

    public String getQueryType(){
        return query_type;
    }

    public void setQueryType(String query_type){
        this.query_type = query_type;
    }
}
