package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.*;
import one.microproject.authx.common.dto.AuthxInfo;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.service.model.Authx;
import one.microproject.authx.service.model.Client;
import one.microproject.authx.service.model.Group;
import one.microproject.authx.service.model.Permission;
import one.microproject.authx.service.model.Project;
import one.microproject.authx.service.model.Role;
import one.microproject.authx.service.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DMapper {

    public ProjectDto map(Project project) {
        return new ProjectDto(project.getId(), project.getDescription(), project.getLabels());
    }

    public Project map(CreateProjectRequest request) {
        return new Project(request.id(), request.description(), List.of(request.adminUser().id()), request.labels());
    }

    public ClientDto map(Client client) {
        return new ClientDto(client.getClientId(), client.getDescription(), client.getLabels(), map(client.getRoles()), map(client.getGroups()));
    }

    public Client map(String dbId, String projectId, String secretHash, CreateClientRequest clientRequest, String defaultKid, Map<String, KeyPairSerialized> keyPairs) {
        return new Client(dbId, clientRequest.id(), projectId, clientRequest.description(),
                secretHash, defaultKid, clientRequest.labels(), keyPairs, map(clientRequest.groups()), map(clientRequest.roles()));
    }

    public UserDto map(User user) {
        return new UserDto(user.getUserId(), user.getClientId(), user.getEmail(), user.getDescription(), user.getLabels(), map(user.getRoles()), map(user.getGroups()));
    }

    public User map(String dbId, String projectId, String clientId, String secretHash, CreateUserRequest request, String defaultKid, Map<String, KeyPairSerialized> keyPairs) {
        return new User(dbId, request.id(), projectId, clientId, request.email(), request.description(), secretHash, defaultKid, request.labels(), keyPairs,
                map(request.groups()), map(request.roles()));
    }

    public Authx map(AuthxDto authxDto) {
        return new Authx(authxDto.id(), authxDto.globalAdminProjectIds());
    }

    public AuthxDto map(Authx authx) {
        return new AuthxDto(authx.getId(), authx.getGlobalAdminProjectIds());
    }

    public KeyPairData map(KeyPairSerialized keyPairSerialized) {
        return CryptoUtils.map(keyPairSerialized);
    }

    public KeyPairSerialized map(KeyPairData keyPairData) {
        return CryptoUtils.map(keyPairData);
    }

    public List<String> map(Set<String> ids) {
        if (ids == null) {
            return List.of();
        }
        return new ArrayList<>(ids);
    }

    public Set<String> map(List<String> ids) {
        if (ids == null) {
            return Set.of();
        }
        return new HashSet<>(ids);
    }

    public GroupDto map(Group group) {
        return new GroupDto(group.getGroupId(), group.getDescription(), group.getLabels());
    }

    public Group map(String dbId, String projectId, GroupDto createGroupRequest) {
        return new Group(dbId, createGroupRequest.id(), projectId, createGroupRequest.description(), createGroupRequest.labels());
    }

    public RoleDto map(Role role) {
        return new RoleDto(role.getRoleId(), role.getDescription(), map(role.getPermissionIds()));
    }

    public Role map(String dbId, String projectId, RoleDto roleDto) {
        return new Role(dbId, roleDto.id(), projectId, roleDto.description(), map(roleDto.permissionIds()));
    }

    public PermissionDto map(Permission permission) {
        return new PermissionDto(permission.getPermissionId(), permission.getDescription(), permission.getService(), permission.getResource(), permission.getAction());
    }

    public Permission map(String dbId, String projectId, PermissionDto permissionDto) {
        return new Permission(dbId, permissionDto.id(), projectId, permissionDto.description(), permissionDto.service(), permissionDto.resource(), permissionDto.action());
    }

}