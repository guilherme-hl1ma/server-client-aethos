/**
 * Representa um tipo que é um Evento de notificação.
 * @param {string} uidUserTo - UID do usuário que recebeu a ação.
 * @param {UserFromDetails} userFromDetails - Informações do usuário que realizou a ação.
 * @param {boolean} like - true caso a operação seja de curtida, senão false.
 * @param {boolean} follow - true caso a operação seja de seguir, senão false.
 * @param {boolean} comment - true caso a operação seja de comentário, senão false.
 * @param {boolean} read - true se o usuário clicou/leu a notificação, senão false.
 */
export type NotificationEvent = {
  uidUserTo: string;
  userFromDetails: UserFromDetails;
  like?: boolean;
  follow?: boolean;
  comment?: boolean;
  read: boolean;
};

type UserFromDetails = {
  uid: string;
  name: string;
};
