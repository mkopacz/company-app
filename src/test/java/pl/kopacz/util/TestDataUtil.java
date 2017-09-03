package pl.kopacz.util;

import pl.kopacz.domain.User;
import pl.kopacz.web.rest.vm.ManagedUserVM;

public class TestDataUtil {

    public static ManagedUserVM generateManagedUserVM() {
        User user = new User();
        user.setLogin("johndoe");
        user.setPassword("johndoe");
        user.setEmail("john.doe@localhost");
        user.setLangKey("en-US");
        return new ManagedUserVM(user);
    }

}
