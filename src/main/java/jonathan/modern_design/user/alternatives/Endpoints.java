package jonathan.modern_design.user.alternatives;

//Controller could be moved to facade since at this point is a middle-man antipattern
//This example shows how to contralize endpoints in a single private file

//@RequestMapping("/api/v1/users")
class UserController {
}

//@RequestMapping("/api/v1/roles")
class RoleController {
    //Code...
}
