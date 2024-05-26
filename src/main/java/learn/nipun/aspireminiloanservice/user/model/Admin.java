package learn.nipun.aspireminiloanservice.user.model;

import java.time.OffsetDateTime;
import lombok.Getter;

@Getter
public class Admin extends User {

    public Admin(OffsetDateTime createdOn, String userName, String name, Address address,
            String phoneNumber, String email, UserStatus userStatus, Integer cibilScore,
            OffsetDateTime updatedOn) {

        super(createdOn, userName, name, address, phoneNumber, email, userStatus, cibilScore, updatedOn);
    }
}
