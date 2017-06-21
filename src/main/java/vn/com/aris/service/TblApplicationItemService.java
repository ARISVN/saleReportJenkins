package vn.com.aris.service;

import vn.com.aris.dao.ApplicationItemDao;

import java.beans.PropertyVetoException;
import java.io.IOException;

public class TblApplicationItemService {
	private ApplicationItemDao itemDao=new ApplicationItemDao();
	public String updateItem(Long idItem,String itemName) throws IOException, PropertyVetoException
	{
		return itemDao.updateItem(idItem,itemName);
		
	}
	public String doDeleteItem(Long idItem) throws IOException, PropertyVetoException {
		return itemDao.deleteItem(idItem);
	}
}
