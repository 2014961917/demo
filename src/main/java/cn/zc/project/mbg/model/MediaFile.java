package cn.zc.project.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class MediaFile implements Serializable {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "文件id")
    private String fileid;

    @ApiModelProperty(value = "文件名称")
    private String filename;

    @ApiModelProperty(value = "文件原始名称")
    private String fileoriginalname;

    @ApiModelProperty(value = "文件路径")
    private String filepath;

    @ApiModelProperty(value = "文件url")
    private String fileurl;

    @ApiModelProperty(value = "文件类型")
    private String filetype;

    @ApiModelProperty(value = "mimeType")
    private String mimetype;

    @ApiModelProperty(value = "文件大小")
    private Long filesize;

    @ApiModelProperty(value = "文件状态")
    private String filestatus;

    @ApiModelProperty(value = "上传时间")
    private Date uploadtime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileoriginalname() {
        return fileoriginalname;
    }

    public void setFileoriginalname(String fileoriginalname) {
        this.fileoriginalname = fileoriginalname;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    public String getFilestatus() {
        return filestatus;
    }

    public void setFilestatus(String filestatus) {
        this.filestatus = filestatus;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fileid=").append(fileid);
        sb.append(", filename=").append(filename);
        sb.append(", fileoriginalname=").append(fileoriginalname);
        sb.append(", filepath=").append(filepath);
        sb.append(", fileurl=").append(fileurl);
        sb.append(", filetype=").append(filetype);
        sb.append(", mimetype=").append(mimetype);
        sb.append(", filesize=").append(filesize);
        sb.append(", filestatus=").append(filestatus);
        sb.append(", uploadtime=").append(uploadtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}