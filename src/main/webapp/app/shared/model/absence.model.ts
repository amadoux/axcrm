import dayjs from 'dayjs';
import { TypeAbsence } from 'app/shared/model/enumerations/type-absence.model';
import { ConfirmationAbsence } from 'app/shared/model/enumerations/confirmation-absence.model';

export interface IAbsence {
  id?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  numberDayAbsence?: number | null;
  typeAbsence?: keyof typeof TypeAbsence | null;
  confirmationAbsence?: keyof typeof ConfirmationAbsence | null;
  congeRestant?: number | null;
}

export const defaultValue: Readonly<IAbsence> = {};
