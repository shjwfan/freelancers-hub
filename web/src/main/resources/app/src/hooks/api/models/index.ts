export enum RequestResult {
  Progress = 'Progress',
  Succeeded = 'Succeeded',
  UnSucceeded = 'UnSucceeded',
}

export type Token = {
  accessToken: string;
  refreshToken: string;
};
