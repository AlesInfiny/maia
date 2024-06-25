import { useAuthenticationStore } from '@/stores/authentication/authentication';

const authenticationStore = useAuthenticationStore();

export async function signInAsync() {
  authenticationStore.signInAsync();
}
