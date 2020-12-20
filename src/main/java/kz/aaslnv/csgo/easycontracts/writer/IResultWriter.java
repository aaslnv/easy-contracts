package kz.aaslnv.csgo.easycontracts.writer;

import kz.aaslnv.csgo.easycontracts.contract.model.Contract;

import java.util.List;

public interface IResultWriter {

    void write(List<Contract> contracts);
}
