import { useUserStore } from '@/stores/user/user';
import { authenticationService } from '@/services/authentication/authentication-service';

export async function fetchUser() {
  const userStore = useUserStore();
  if (!(await authenticationService.isAuthenticated())) {
    return;
  }
  await userStore.fetchUserResponse();
}
