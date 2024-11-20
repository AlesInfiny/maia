import { useAuthenticationStore } from '@/stores/authentication/authentication';

export async function loginAsync() {
  const authenticationStore = useAuthenticationStore();
  await authenticationStore.signInAsync();
}

export async function logoutAsync() {
  const authenticationStore = useAuthenticationStore();
  await authenticationStore.signOutAsync();
}
