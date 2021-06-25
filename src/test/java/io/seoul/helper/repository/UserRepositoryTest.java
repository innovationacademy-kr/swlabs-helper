package io.seoul.helper.repository;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {
    /*
    @Autowired
    UserRepository repo;

    ArrayList<User> userlist;

    public UserRepositoryTest() {
    }

    @BeforeAll
    public void setup() {
        userlist = new ArrayList<>();
        userlist.add(User.builder()
                .nickname("help_tester_1")
                .fullname("Hyunjin Won")
                .email("tester1@mail.com")
                .picture("")
                .role(Role.USER)
                .build());

        userlist.add(User.builder()
                .nickname("help_tester_2")
                .fullname("Yungho Choi")
                .email("tester2@mail.com")
                .picture("")
                .role(Role.USER)
                .build()
        );
        userlist.add(User.builder()
                .nickname("help_tester_3")
                .fullname("Mincheol Kang")
                .email("tester3@mail.com")
                .picture("")
                .role(Role.USER)
                .build()
        );
        userlist.add(User.builder()
                .nickname("help_tester_4")
                .fullname("Gwangnam Heo")
                .email("tester4@mail.com")
                .picture("")
                .role(Role.ADMIN)
                .build()
        );
        for (User user : userlist) {
            repo.save(user);
        }
    }

    @AfterAll
    public void cleanup() {
        for (User user : userlist) {
            Optional<User> target = repo.findUserByNickname(user.getNickname());
            target.ifPresent(o -> repo.delete(o));
        }
    }

    @Test
    public void saveTest() {
        User target;
        target = User.builder()
                .nickname("help_tester5")
                .fullname("tester")
                .email("tester@mail.com")
                .picture("none")
                .role(Role.USER)
                .build();
        userlist.add(target);
        User saved = repo.save(target);
        assertThat(saved.getId(), is(notNullValue()));
        checkUserEqual(target, saved);
    }

    @Test
    public void deleteTest() {
        List<User> lists = repo.findAll();
        User target = lists.get(0);

        repo.delete(target);
        Optional<User> rst = repo.findById(target.getId());
        rst.ifPresent(o -> fail( "fail : target user is not deleted User : " + o.getNickname()));
        userlist.remove(0);
    }

    @Test
    public void findUserByEmailTest() {
        for (User user: userlist) {
            Optional<User> find = repo.findUserByEmail(user.getEmail());
            User cmp = Optional.ofNullable(find).map(o -> o.get()).orElseGet(() ->
                    fail("fail : cannot find User : " + user.getEmail()));
            checkUserEqual(cmp, user);
        }
    }

    @Test
    public void findUserByNicknameTest() {
        for (User user: userlist) {
            Optional<User> find = repo.findUserByNickname(user.getNickname());
            User cmp = Optional.ofNullable(find).map(o -> o.get()).orElseGet(() ->
                    fail("fail : cannot find User : " + user.getNickname()));
            checkUserEqual(cmp, user);
        }
    }

    @Test
    public void updateUserTest() {
        User bfuser = userlist.get(0);
        User afuser = User.builder()
                .nickname("help_tester0")
                .picture("new_pic_url")
                .email(bfuser.getEmail())
                .role(bfuser.getRole())
                .fullname("new_fullname")
                .build();
        Optional<User> find = repo.findById(bfuser.getId());
        User target = Optional.ofNullable(find).map(o -> o.get()).orElseGet(() ->
                fail("fail : cannot find User : " + afuser.getNickname()));
        target.update(afuser.getNickname(), afuser.getFullname(), afuser.getPicture());
        repo.save(target);
        checkUserEqual(target, afuser);
        userlist.set(0, target);
    }

    public void checkUserEqual(User u1, User u2) {
        assertThat(u1.getFullname(), is(equalTo(u2.getFullname())));
        assertThat(u1.getNickname(), is(equalTo(u2.getNickname())));
        assertThat(u1.getEmail(), is(equalTo(u2.getEmail())));
        assertThat(u1.getPicture(), is(equalTo(u2.getPicture())));
        assertThat(u1.getRole(), is(equalTo(u2.getRole())));
    }
     */
}
