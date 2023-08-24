/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.chat.kafka.avro.model;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class UserAvroModel extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 423690647297211075L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UserAvroModel\",\"namespace\":\"com.chat.kafka.avro.model\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"name\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"}]},{\"name\":\"username\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"}]},{\"name\":\"createdAt\",\"type\":[\"null\",\"long\"],\"logicalType\":[\"null\",\"date\"]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<UserAvroModel> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<UserAvroModel> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<UserAvroModel> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<UserAvroModel> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<UserAvroModel> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this UserAvroModel to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a UserAvroModel from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a UserAvroModel instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static UserAvroModel fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private long id;
  private java.lang.Object name;
  private java.lang.Object username;
  private java.lang.Long createdAt;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public UserAvroModel() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param name The new value for name
   * @param username The new value for username
   * @param createdAt The new value for createdAt
   */
  public UserAvroModel(java.lang.Long id, java.lang.Object name, java.lang.Object username, java.lang.Long createdAt) {
    this.id = id;
    this.name = name;
    this.username = username;
    this.createdAt = createdAt;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return name;
    case 2: return username;
    case 3: return createdAt;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: name = value$; break;
    case 2: username = value$; break;
    case 3: createdAt = (java.lang.Long)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public long getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'name' field.
   * @return The value of the 'name' field.
   */
  public java.lang.Object getName() {
    return name;
  }


  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(java.lang.Object value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'username' field.
   * @return The value of the 'username' field.
   */
  public java.lang.Object getUsername() {
    return username;
  }


  /**
   * Sets the value of the 'username' field.
   * @param value the value to set.
   */
  public void setUsername(java.lang.Object value) {
    this.username = value;
  }

  /**
   * Gets the value of the 'createdAt' field.
   * @return The value of the 'createdAt' field.
   */
  public java.lang.Long getCreatedAt() {
    return createdAt;
  }


  /**
   * Sets the value of the 'createdAt' field.
   * @param value the value to set.
   */
  public void setCreatedAt(java.lang.Long value) {
    this.createdAt = value;
  }

  /**
   * Creates a new UserAvroModel RecordBuilder.
   * @return A new UserAvroModel RecordBuilder
   */
  public static com.chat.kafka.avro.model.UserAvroModel.Builder newBuilder() {
    return new com.chat.kafka.avro.model.UserAvroModel.Builder();
  }

  /**
   * Creates a new UserAvroModel RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new UserAvroModel RecordBuilder
   */
  public static com.chat.kafka.avro.model.UserAvroModel.Builder newBuilder(com.chat.kafka.avro.model.UserAvroModel.Builder other) {
    if (other == null) {
      return new com.chat.kafka.avro.model.UserAvroModel.Builder();
    } else {
      return new com.chat.kafka.avro.model.UserAvroModel.Builder(other);
    }
  }

  /**
   * Creates a new UserAvroModel RecordBuilder by copying an existing UserAvroModel instance.
   * @param other The existing instance to copy.
   * @return A new UserAvroModel RecordBuilder
   */
  public static com.chat.kafka.avro.model.UserAvroModel.Builder newBuilder(com.chat.kafka.avro.model.UserAvroModel other) {
    if (other == null) {
      return new com.chat.kafka.avro.model.UserAvroModel.Builder();
    } else {
      return new com.chat.kafka.avro.model.UserAvroModel.Builder(other);
    }
  }

  /**
   * RecordBuilder for UserAvroModel instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<UserAvroModel>
    implements org.apache.avro.data.RecordBuilder<UserAvroModel> {

    private long id;
    private java.lang.Object name;
    private java.lang.Object username;
    private java.lang.Long createdAt;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.chat.kafka.avro.model.UserAvroModel.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.username)) {
        this.username = data().deepCopy(fields()[2].schema(), other.username);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.createdAt)) {
        this.createdAt = data().deepCopy(fields()[3].schema(), other.createdAt);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing UserAvroModel instance
     * @param other The existing instance to copy.
     */
    private Builder(com.chat.kafka.avro.model.UserAvroModel other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.username)) {
        this.username = data().deepCopy(fields()[2].schema(), other.username);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.createdAt)) {
        this.createdAt = data().deepCopy(fields()[3].schema(), other.createdAt);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public long getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.chat.kafka.avro.model.UserAvroModel.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public com.chat.kafka.avro.model.UserAvroModel.Builder clearId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'name' field.
      * @return The value.
      */
    public java.lang.Object getName() {
      return name;
    }


    /**
      * Sets the value of the 'name' field.
      * @param value The value of 'name'.
      * @return This builder.
      */
    public com.chat.kafka.avro.model.UserAvroModel.Builder setName(java.lang.Object value) {
      validate(fields()[1], value);
      this.name = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'name' field has been set.
      * @return True if the 'name' field has been set, false otherwise.
      */
    public boolean hasName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'name' field.
      * @return This builder.
      */
    public com.chat.kafka.avro.model.UserAvroModel.Builder clearName() {
      name = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'username' field.
      * @return The value.
      */
    public java.lang.Object getUsername() {
      return username;
    }


    /**
      * Sets the value of the 'username' field.
      * @param value The value of 'username'.
      * @return This builder.
      */
    public com.chat.kafka.avro.model.UserAvroModel.Builder setUsername(java.lang.Object value) {
      validate(fields()[2], value);
      this.username = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'username' field has been set.
      * @return True if the 'username' field has been set, false otherwise.
      */
    public boolean hasUsername() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'username' field.
      * @return This builder.
      */
    public com.chat.kafka.avro.model.UserAvroModel.Builder clearUsername() {
      username = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'createdAt' field.
      * @return The value.
      */
    public java.lang.Long getCreatedAt() {
      return createdAt;
    }


    /**
      * Sets the value of the 'createdAt' field.
      * @param value The value of 'createdAt'.
      * @return This builder.
      */
    public com.chat.kafka.avro.model.UserAvroModel.Builder setCreatedAt(java.lang.Long value) {
      validate(fields()[3], value);
      this.createdAt = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'createdAt' field has been set.
      * @return True if the 'createdAt' field has been set, false otherwise.
      */
    public boolean hasCreatedAt() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'createdAt' field.
      * @return This builder.
      */
    public com.chat.kafka.avro.model.UserAvroModel.Builder clearCreatedAt() {
      createdAt = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserAvroModel build() {
      try {
        UserAvroModel record = new UserAvroModel();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.name = fieldSetFlags()[1] ? this.name :  defaultValue(fields()[1]);
        record.username = fieldSetFlags()[2] ? this.username :  defaultValue(fields()[2]);
        record.createdAt = fieldSetFlags()[3] ? this.createdAt : (java.lang.Long) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<UserAvroModel>
    WRITER$ = (org.apache.avro.io.DatumWriter<UserAvroModel>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<UserAvroModel>
    READER$ = (org.apache.avro.io.DatumReader<UserAvroModel>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}










