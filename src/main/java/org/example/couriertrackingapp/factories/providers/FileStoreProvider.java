package org.example.couriertrackingapp.factories.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.couriertrackingapp.domain.entities.Store;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

import static org.example.couriertrackingapp.utils.FileUtils.readListFromJson;

@Component("fileStoreProvider")
public class FileStoreProvider implements StoreProvider {


    @Override
    public List<Store> getStores() {
        try {
            File file = new ClassPathResource("files/stores.json").getFile();
            return readListFromJson(file, new TypeReference<>() {});
        }
        catch (Exception e){
            e.printStackTrace();
            return List.of();
        }
    }
}
