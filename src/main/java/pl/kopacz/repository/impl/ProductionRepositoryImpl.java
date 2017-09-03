package pl.kopacz.repository.impl;

import org.hibernate.Session;
import pl.kopacz.domain.Product;
import pl.kopacz.domain.Production;
import pl.kopacz.repository.custom.ProductionRepositoryCustom;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class ProductionRepositoryImpl implements ProductionRepositoryCustom {

    @Inject
    private EntityManager entityManager;

    public void loadProducts(Production production) {
        Session session = entityManager.unwrap(Session.class);
        production.getProductionItems().forEach(productionItem -> {
            Product product = productionItem.getProduct();
            session.load(product, product.getId());
        });
    }

}
