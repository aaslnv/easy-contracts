package kz.aaslnv.csgo.easycontracts.writer;

import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "file")
public class FileResultWriter implements IResultWriter{

    @Override
    public void write(List<Contract> contracts) {
        System.out.println("will add functionality of saving result to file");
    }
}
