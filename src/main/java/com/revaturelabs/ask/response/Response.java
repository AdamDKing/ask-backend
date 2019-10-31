package com.revaturelabs.ask.response;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Holds the information about a response to a question. It holds the id of the responder, it's
 * title and body, as well as it's creation date.
 * 
 * @author Bryan Ritter, Chris Allen
 *
 */

@Entity
@Table(name = "responses")
@JsonDeserialize(using = ResponseJSonDeserializer.class)
public class Response {

  @Id
  @Column(name = "id")
  @GeneratedValue
  private int id;

  @Column(name = "responder_id")
  private Integer responderId;

  @Column(name = "body")
  private String body;

  @Column(name = "creation_date", updatable = false)
  private Date creationDate;

  @Column(name = "question_id")
  private Integer questionId;

  public Response() {

  }

  /**
   * Auto-generated getter for id.
   *
   * @return the id
   */
  public int getId() {
    return this.id;
  }

  /**
   * Auto-generated setter for id.
   * 
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }
  
  /** 
   * Auto-generated getter for the question id.
   * 
   * @return the corresponding question's id
   */
  public Integer getQuestionId() {
    return questionId;
  }

  /**
   * Auto-generated setter for the question id.
   * 
   * @param questionId the id of a question this is a response to
   */
  public void setQuestionId(Integer questionId) {
    this.questionId = questionId;
  }


  /**
   * Auto-generated getter for the responder's id.
   * 
   * @return the responderId
   */
  public Integer getResponderId() {
    return this.responderId;
  }

  /**
   * Auto-generated setter for the responder's id.
   * 
   * @param responderId the responderId to set
   */
  public void setResponderId(Integer responderId) {
    this.responderId = responderId;
  }

  /**
   * Auto-generated getter for the body's text.
   * 
   * @return the body
   */
  public String getBody() {
    return this.body;
  }

  /**
   * Auto-generated setter for the body's text.
   * 
   * @param body the body to set
   */
  public void setBody(String body) {
    this.body = body;
  }

  /**
   * Auto-generated getter for the creation date.
   * 
   * @return the creationDate
   */
  public Date getCreationDate() {
    return this.creationDate;
  }

  /**
   * @param creationDate the creationDate to set
   */
  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * @return the questionId
   */
  public Integer getQuestionId() {
    return this.questionId;
  }

  /**
   * Auto-generated setter for the creation date.
   * 
   * @param questionId the questionId to set
   */
  public void setQuestionId(Integer questionId) {
    this.questionId = questionId;
  }

  /**
   * Auto-generated toString method
   * 
   * @return String representation of Response
   */
  @Override
  public String toString() {
    return "Response [id=" + this.id + ", responderId=" + this.responderId + ", body=" + this.body
        + ", creationDate=" + this.creationDate + ", questionId=" + this.questionId + "]";
  }

}

