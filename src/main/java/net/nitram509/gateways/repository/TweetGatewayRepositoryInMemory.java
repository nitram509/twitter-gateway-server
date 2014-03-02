package net.nitram509.gateways.repository;

import net.nitram509.gateways.api.Gateway;
import net.nitram509.gateways.api.GatewayId;
import net.nitram509.gateways.api.UserId;
import net.nitram509.gateways.api.UserProfile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class TweetGatewayRepositoryInMemory implements TweetGatewayRepository {

  private final List<UserProfile> profiles = new ArrayList<>();
  private final List<Gateway> gateways = new ArrayList<>();

  TweetGatewayRepositoryInMemory() {
    // singleton, make constructor private
  }

  @Override
  public void save(UserProfile userProfile) {
    profiles.add(userProfile);
  }

  @Override
  public void save(Gateway gateway) {
    gateways.add(gateway);
  }

  @Override
  public UserProfile getUser(UserId userId) {
    for (UserProfile profile : profiles) {
      if (profile.getId().equals(userId)) {
        return profile;
      }
    }
    throw new NoSuchElementException("UserProfile doesn't exists for " + userId);
  }

  @Override
  public List<Gateway> findGateways(UserId owner) {
    List<Gateway> result = new ArrayList<>(3);
    for (Gateway gateway : gateways) {
      if (gateway.getOwner().equals(owner)) {
        result.add(gateway);
      }
    }
    return result;
  }

  @Override
  public Gateway getGateway(GatewayId gatewayId) {
    Gateway result = null;
    for (Gateway gateway : gateways) {
      if (gateway.getId().equals(gatewayId)) {
        result = gateway;
        break;
      }
    }
    return result;
  }

  @Override
  public void incrementActivity(GatewayId gatewayId) {
    Gateway gateway = getGateway(gatewayId);
    int activity = gateway.getActivity();
    gateway.setActivity(activity + 1);
    save(gateway);
  }

  @Override
  public void remove(GatewayId gatewayId) {
    Iterator<Gateway> iterator = gateways.iterator();
    while (iterator.hasNext()) {
      if (gatewayId.equals(iterator.next().getId())) {
        iterator.remove();
        break;
      }
    }
  }
}
