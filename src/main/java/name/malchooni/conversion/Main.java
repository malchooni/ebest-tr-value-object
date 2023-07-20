package name.malchooni.conversion;

import name.malchooni.conversion.ebest.Convertor;
import name.malchooni.conversion.ebest.res.ResDataRepository;
import name.malchooni.conversion.ebest.res.ResMessageExecutor;

/**
 * 시작 클래스
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // arguments to property object
        Property property = new Property(args);

        // ebest res file read
        ResMessageExecutor executor = new ResMessageExecutor();
        executor.setResRootPath(property.getResRootPath());
        executor.loadResFiles();

        ResDataRepository repository = executor.getResDataRepository();

        // create ebest VO java file
        Convertor convertor = new Convertor(property);
        convertor.convertMessage(repository.getResQuery());
        convertor.convertMessage(repository.getResReal());

        System.exit(0);
    }
}