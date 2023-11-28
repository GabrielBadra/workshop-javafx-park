package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;
import model.exceptions.StringEqualsException;

public class SellerService {

	SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){		
		return dao.findAll();
	}
	
	public void saveOrUptade(Seller seller) {
		if(seller.getId() == null) {
			List<Seller> sellers = findAll();
			
			if(sellers == null){
				dao.insert(seller);
			}
			
			for(Seller obj: sellers) {
				if(seller.getPlaca().toLowerCase().equals(obj.getPlaca().toLowerCase())) {
					throw new StringEqualsException("Placa ja utilizada!");
				}
			}

			dao.insert(seller);
		}else {
			dao.update(seller);
		}
	}
	
	public void remove(Seller seller) {
		dao.deleteById(seller.getId());
	}
}
