package com.land.server.domain;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

@Entity
@Table(name = "T_FILE_CONTENT")
public class BlobEntity extends AbstractEntity {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column
    private Blob content;

    @OneToOne(mappedBy = "blob")
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public InputStream getContentInputStream() throws SQLException {
        return content.getBinaryStream();
    }

    public void setContent(byte[] content) throws SerialException, SQLException {
        this.content = new SerialBlob(content);
    }

}
