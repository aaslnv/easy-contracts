package kz.aaslnv.csgo.easycontracts.parser;

import java.util.List;

public interface IParser<T extends Parsable> {

    List<T> parse();
}
