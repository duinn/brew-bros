import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBeerOption, defaultValue } from 'app/shared/model/beer-option.model';

export const ACTION_TYPES = {
  FETCH_BEEROPTION_LIST: 'beerOption/FETCH_BEEROPTION_LIST',
  FETCH_BEEROPTION: 'beerOption/FETCH_BEEROPTION',
  CREATE_BEEROPTION: 'beerOption/CREATE_BEEROPTION',
  UPDATE_BEEROPTION: 'beerOption/UPDATE_BEEROPTION',
  DELETE_BEEROPTION: 'beerOption/DELETE_BEEROPTION',
  RESET: 'beerOption/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBeerOption>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type BeerOptionState = Readonly<typeof initialState>;

// Reducer

export default (state: BeerOptionState = initialState, action): BeerOptionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BEEROPTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BEEROPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BEEROPTION):
    case REQUEST(ACTION_TYPES.UPDATE_BEEROPTION):
    case REQUEST(ACTION_TYPES.DELETE_BEEROPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BEEROPTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BEEROPTION):
    case FAILURE(ACTION_TYPES.CREATE_BEEROPTION):
    case FAILURE(ACTION_TYPES.UPDATE_BEEROPTION):
    case FAILURE(ACTION_TYPES.DELETE_BEEROPTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BEEROPTION_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_BEEROPTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BEEROPTION):
    case SUCCESS(ACTION_TYPES.UPDATE_BEEROPTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BEEROPTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/beer-options';

// Actions

export const getEntities: ICrudGetAllAction<IBeerOption> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BEEROPTION_LIST,
    payload: axios.get<IBeerOption>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IBeerOption> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BEEROPTION,
    payload: axios.get<IBeerOption>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IBeerOption> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BEEROPTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IBeerOption> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BEEROPTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBeerOption> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BEEROPTION,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
