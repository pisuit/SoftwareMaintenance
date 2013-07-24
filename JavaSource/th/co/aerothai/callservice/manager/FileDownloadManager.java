package th.co.aerothai.callservice.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;

import th.co.aerothai.callservice.model.Attachment;
import th.co.aerothai.callservice.utils.CharacterUtils;
import th.co.aerothai.callservice.utils.SVNUtils;

@ManagedBean(name="download")
@RequestScoped
public class FileDownloadManager {
	public void download(Attachment file) throws IOException{
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		InputStream is = null;
		byte[] data;
		try {
			is = SVNUtils.downloadFile(file.getLogicalName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = IOUtils.toByteArray(is);
		
		ec.setResponseContentType(file.getContentType());
		ec.setResponseContentLength(data.length);
		ec.setResponseCharacterEncoding("UTF-8");
		ec.setResponseHeader("Content-Disposition", "attachment;charset=utf-8; filename=\""+CharacterUtils.Unicode2ASCII(file.getPhysicalName()) + "\""); 
		
		try {
			OutputStream out = ec.getResponseOutputStream();
			out.write(data);

			fc.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
